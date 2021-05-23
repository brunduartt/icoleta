import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICollectPoint, CollectPoint } from 'app/shared/model/collect-point.model';
import { CollectPointService } from './collect-point.service';
import { IMaterial, Material } from 'app/shared/model/material.model';
import { MaterialService } from 'app/entities/material/material.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { MaterialType } from 'app/shared/model/enumerations/material-type.model';
import { circle, latLng, polygon, tileLayer, Map, Marker, marker, LatLng, Icon } from 'leaflet';
import { DomSanitizer } from '@angular/platform-browser';

type SelectableEntity = IMaterial | IUser;

@Component({
  selector: 'jhi-collect-point-update',
  templateUrl: './collect-point-update.component.html',
  styleUrls: ['collect-point.scss']
})
export class CollectPointUpdateComponent implements OnInit {
  isSaving = false;
  materials: IMaterial[] = [];
  users: IUser[] = [];
  MaterialType = MaterialType;
  positionMarker: Marker | undefined;
  map: Map | undefined;
  center: LatLng | undefined;
  canEdit = false;
  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    lat: [null, [Validators.required]],
    lon: [null, [Validators.required]],
    materials: [],
    users: []
  });
  options = {
    layers: [tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 20, attribution: '' })],
    zoom: 15,
    center: latLng(-19.912998, -43.940933)
  };
  constructor(
    protected collectPointService: CollectPointService,
    protected materialService: MaterialService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private domSanitizer: DomSanitizer,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.center = new LatLng(-19.912998, -43.940933);
    this.activatedRoute.data.subscribe(data => {
      const collectPoint = data['collectPoint'];
      this.canEdit = data['canEdit'];
      this.materialService.query().subscribe((res: HttpResponse<IMaterial[]>) => {
        this.materials = res.body || [];
        this.updateForm(collectPoint);
      });
      if (!this.canEdit) {
        this.editForm.disable();
      }
      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  onMapReady(map: Map): any {
    this.map = map;
    this.map.on('click', e => {
      console.log(this.canEdit);
      if (this.canEdit) {
        if (this.positionMarker) {
          // check
          this.map!.removeLayer(this.positionMarker); // remove
        }
        this.positionMarker = marker(e['latlng']);
        this.editForm.get('lat')!.setValue(e['latlng'].lat);
        this.editForm.get('lon')!.setValue(e['latlng'].lng);
        this.positionMarker.addTo(this.map!);
      }
    });
  }

  updateForm(collectPoint: ICollectPoint): void {
    this.editForm.patchValue({
      id: collectPoint.id,
      name: collectPoint.name,
      description: collectPoint.description,
      lat: collectPoint.lat,
      lon: collectPoint.lon,
      materials: collectPoint.materials || [],
      users: collectPoint.users
    });
    if (collectPoint.materials) {
      collectPoint.materials.forEach(material => {
        const index = this.materials.findIndex(m => m.id === material.id);
        if (index >= 0) {
          this.materials[index].checked = true;
        }
      });
    }
    if (collectPoint.lat && collectPoint.lon) {
      const pointLatLng = new LatLng(collectPoint.lat, collectPoint.lon);
      this.center = pointLatLng;
      this.positionMarker = marker(pointLatLng);
      this.positionMarker.addTo(this.map!);
    }
  }

  previousState(): void {
    window.history.back();
  }

  getMaterialTypeClass(materialOption: IMaterial): any {
    return (
      'material-option material-type-' +
      materialOption.materialType!.toString().toLowerCase() +
      ' ' +
      (materialOption.checked ? 'checked' : 'not-checked')
    );
  }

  save(): void {
    this.isSaving = true;
    const collectPoint = this.createFromForm();
    if (collectPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.collectPointService.update(collectPoint));
    } else {
      this.subscribeToSaveResponse(this.collectPointService.create(collectPoint));
    }
  }

  addRemoveMaterial(material: Material): void {
    if (this.editForm.get('materials')!.enabled) {
      material.checked = !material.checked;
    }
  }

  private createFromForm(): ICollectPoint {
    const list = this.materials.filter(m => m.checked);
    return {
      ...new CollectPoint(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lon: this.editForm.get(['lon'])!.value,
      materials: list,
      users: this.editForm.get(['users'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollectPoint>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: SelectableEntity[], option: SelectableEntity): SelectableEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    console.log(option);
    return option;
  }
}

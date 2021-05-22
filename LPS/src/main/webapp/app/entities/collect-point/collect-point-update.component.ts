import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICollectPoint, CollectPoint } from 'app/shared/model/collect-point.model';
import { CollectPointService } from './collect-point.service';
import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from 'app/entities/material/material.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = IMaterial | IUser;

@Component({
  selector: 'jhi-collect-point-update',
  templateUrl: './collect-point-update.component.html'
})
export class CollectPointUpdateComponent implements OnInit {
  isSaving = false;
  materials: IMaterial[] = [];
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    lat: [null, [Validators.required]],
    lon: [null, [Validators.required]],
    materials: [],
    users: []
  });

  constructor(
    protected collectPointService: CollectPointService,
    protected materialService: MaterialService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collectPoint }) => {
      this.updateForm(collectPoint);

      this.materialService.query().subscribe((res: HttpResponse<IMaterial[]>) => (this.materials = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(collectPoint: ICollectPoint): void {
    this.editForm.patchValue({
      id: collectPoint.id,
      name: collectPoint.name,
      description: collectPoint.description,
      lat: collectPoint.lat,
      lon: collectPoint.lon,
      materials: collectPoint.materials,
      users: collectPoint.users
    });
  }

  previousState(): void {
    window.history.back();
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

  private createFromForm(): ICollectPoint {
    return {
      ...new CollectPoint(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lon: this.editForm.get(['lon'])!.value,
      materials: this.editForm.get(['materials'])!.value,
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
    return option;
  }
}

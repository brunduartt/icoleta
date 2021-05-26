import { Component, OnInit, OnDestroy, ViewChild, NgZone } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICollectPoint } from 'app/shared/model/collect-point.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from 'app/entities/material/material.service';
import { CollectPointService } from '../collect-point.service';
import { Marker, LatLng, tileLayer, latLng, Map, LayerGroup, Layer, marker, LatLngExpression, MarkerOptions } from 'leaflet';
import { MaterialSelectorComponent } from '../material-selector/material-selector.component';

class IdMarker extends Marker {
  id!: number;
  constructor(latlng: LatLngExpression, id: number, options?: MarkerOptions) {
    super(latlng, options);
    this.id = id;
  }
}

@Component({
  selector: 'jhi-collect-point-map',
  templateUrl: './collect-point-map.component.html',
  styleUrls: ['../collect-point.scss']
})
export class CollectPointMapComponent implements OnInit {
  collectPoints?: ICollectPoint[];
  totalItems = 0;
  markers: IdMarker[] = [];
  map: Map | undefined;
  center: LatLng | undefined;
  options = {
    layers: [tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 20, attribution: '' })],
    zoom: 15,
    center: latLng(-19.912998, -43.940933)
  };
  @ViewChild('selector') materialSelector: MaterialSelectorComponent | undefined;
  constructor(
    protected collectPointService: CollectPointService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected materialService: MaterialService,
    private ngZone: NgZone
  ) {}

  filter(): any {
    const query = {};
    if (this.materialSelector) {
      const materials = this.materialSelector.getCheckedMaterials().map(m => m.id);
      if (materials) {
        query['materialsId.in'] = materials;
      }
    }
    return query;
  }

  loadPage(): void {
    this.collectPointService
      .query({
        ...this.filter(),
        pageable: false
      })
      .subscribe((res: HttpResponse<ICollectPoint[]>) => this.onSuccess(res.body, res.headers));
  }

  ngOnInit(): void {
    this.loadPage();
  }

  addListCollectPointsToMap(): void {
    if (this.map) {
      this.markers.forEach(m => {
        console.log(m);
        this.map!.removeLayer(m);
      });
      this.markers = [];
      if (this.collectPoints) {
        this.collectPoints.forEach((collectPoint, i) => {
          if (collectPoint.lat && collectPoint.lon) {
            const pointLatLng = new LatLng(collectPoint.lat, collectPoint.lon);
            const pointMarker = new IdMarker(pointLatLng, collectPoint.id!, {
              title: collectPoint.name
            }).on('click', e => {
              this.ngZone.run(() => {
                const clickedMarker = e.target as IdMarker;
                this.router.navigate(['/collect-point', clickedMarker.id, 'view']);
              });
            });
            console.log(i);
            this.map!.addLayer(pointMarker);
            this.markers.push(pointMarker);
          }
        });
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  onMapReady(map: Map): any {
    this.map = map;
    this.addListCollectPointsToMap();
  }

  protected onSuccess(data: ICollectPoint[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.collectPoints = data || [];
    this.addListCollectPointsToMap();
  }
}

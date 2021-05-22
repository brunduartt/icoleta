import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICollectPoint } from 'app/shared/model/collect-point.model';

@Component({
  selector: 'jhi-collect-point-detail',
  templateUrl: './collect-point-detail.component.html'
})
export class CollectPointDetailComponent implements OnInit {
  collectPoint: ICollectPoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collectPoint }) => (this.collectPoint = collectPoint));
  }

  previousState(): void {
    window.history.back();
  }
}

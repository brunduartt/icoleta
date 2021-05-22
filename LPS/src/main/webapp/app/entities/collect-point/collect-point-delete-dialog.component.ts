import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICollectPoint } from 'app/shared/model/collect-point.model';
import { CollectPointService } from './collect-point.service';

@Component({
  templateUrl: './collect-point-delete-dialog.component.html'
})
export class CollectPointDeleteDialogComponent {
  collectPoint?: ICollectPoint;

  constructor(
    protected collectPointService: CollectPointService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.collectPointService.delete(id).subscribe(() => {
      this.eventManager.broadcast('collectPointListModification');
      this.activeModal.close();
    });
  }
}

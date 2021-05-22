import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterial } from 'app/shared/model/material.model';
import { MaterialService } from './material.service';

@Component({
  templateUrl: './material-delete-dialog.component.html'
})
export class MaterialDeleteDialogComponent {
  material?: IMaterial;

  constructor(protected materialService: MaterialService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materialService.delete(id).subscribe(() => {
      this.eventManager.broadcast('materialListModification');
      this.activeModal.close();
    });
  }
}

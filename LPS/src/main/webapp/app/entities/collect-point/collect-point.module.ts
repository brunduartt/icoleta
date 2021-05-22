import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IColetaSharedModule } from 'app/shared/shared.module';
import { CollectPointComponent } from './collect-point.component';
import { CollectPointDetailComponent } from './collect-point-detail.component';
import { CollectPointUpdateComponent } from './collect-point-update.component';
import { CollectPointDeleteDialogComponent } from './collect-point-delete-dialog.component';
import { collectPointRoute } from './collect-point.route';

@NgModule({
  imports: [IColetaSharedModule, RouterModule.forChild(collectPointRoute)],
  declarations: [CollectPointComponent, CollectPointDetailComponent, CollectPointUpdateComponent, CollectPointDeleteDialogComponent],
  entryComponents: [CollectPointDeleteDialogComponent]
})
export class IColetaCollectPointModule {}

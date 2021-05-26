import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IColetaSharedModule } from 'app/shared/shared.module';
import { CollectPointComponent } from './collect-point.component';
import { CollectPointDetailComponent } from './collect-point-detail.component';
import { CollectPointUpdateComponent } from './collect-point-update.component';
import { CollectPointDeleteDialogComponent } from './collect-point-delete-dialog.component';
import { collectPointRoute } from './collect-point.route';
import { MaterialSelectorComponent } from './material-selector/material-selector.component';
import { CollectPointMapComponent } from './collect-point-map/collect-point-map.component';

@NgModule({
  imports: [IColetaSharedModule, RouterModule.forChild(collectPointRoute)],
  declarations: [
    CollectPointComponent,
    CollectPointDetailComponent,
    CollectPointUpdateComponent,
    CollectPointDeleteDialogComponent,
    MaterialSelectorComponent,
    CollectPointMapComponent
  ],
  entryComponents: [CollectPointDeleteDialogComponent]
})
export class IColetaCollectPointModule {}

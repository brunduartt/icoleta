import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'material',
        loadChildren: () => import('./material/material.module').then(m => m.IColetaMaterialModule)
      },
      {
        path: 'collect-point',
        loadChildren: () => import('./collect-point/collect-point.module').then(m => m.IColetaCollectPointModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class IColetaEntityModule {}

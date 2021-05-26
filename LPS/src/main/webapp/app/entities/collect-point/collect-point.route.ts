import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICollectPoint, CollectPoint } from 'app/shared/model/collect-point.model';
import { CollectPointService } from './collect-point.service';
import { CollectPointComponent } from './collect-point.component';
import { CollectPointDetailComponent } from './collect-point-detail.component';
import { CollectPointUpdateComponent } from './collect-point-update.component';
import { CollectPointMapComponent } from './collect-point-map/collect-point-map.component';

@Injectable({ providedIn: 'root' })
export class CollectPointResolve implements Resolve<ICollectPoint> {
  constructor(private service: CollectPointService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICollectPoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((collectPoint: HttpResponse<CollectPoint>) => {
          if (collectPoint.body) {
            return of(collectPoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CollectPoint());
  }
}

export const collectPointRoute: Routes = [
  {
    path: '',
    component: CollectPointMapComponent,
    data: {
      pageTitle: 'CollectPoints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'table',
    component: CollectPointComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'CollectPoints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CollectPointUpdateComponent,
    runGuardsAndResolvers: 'always',
    resolve: {
      collectPoint: CollectPointResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'CollectPoints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CollectPointUpdateComponent,
    resolve: {
      collectPoint: CollectPointResolve
    },
    data: {
      canEdit: true,
      authorities: [Authority.USER],
      pageTitle: 'CollectPoints'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CollectPointUpdateComponent,
    resolve: {
      collectPoint: CollectPointResolve
    },
    data: {
      canEdit: true,
      authorities: [Authority.USER],
      pageTitle: 'CollectPoints'
    },
    canActivate: [UserRouteAccessService]
  }
];

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICollectPoint } from 'app/shared/model/collect-point.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CollectPointService } from './collect-point.service';
import { CollectPointDeleteDialogComponent } from './collect-point-delete-dialog.component';

@Component({
  selector: 'jhi-collect-point',
  templateUrl: './collect-point.component.html',
  styleUrls: ['collect-point.scss']
})
export class CollectPointComponent implements OnInit, OnDestroy {
  collectPoints?: ICollectPoint[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected collectPointService: CollectPointService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.collectPointService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ICollectPoint[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInCollectPoints();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICollectPoint): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCollectPoints(): void {
    this.eventSubscriber = this.eventManager.subscribe('collectPointListModification', () => this.loadPage());
  }

  delete(collectPoint: ICollectPoint): void {
    const modalRef = this.modalService.open(CollectPointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.collectPoint = collectPoint;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ICollectPoint[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/collect-point'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.collectPoints = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}

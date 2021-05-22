import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IColetaTestModule } from '../../../test.module';
import { CollectPointDetailComponent } from 'app/entities/collect-point/collect-point-detail.component';
import { CollectPoint } from 'app/shared/model/collect-point.model';

describe('Component Tests', () => {
  describe('CollectPoint Management Detail Component', () => {
    let comp: CollectPointDetailComponent;
    let fixture: ComponentFixture<CollectPointDetailComponent>;
    const route = ({ data: of({ collectPoint: new CollectPoint(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IColetaTestModule],
        declarations: [CollectPointDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CollectPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CollectPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load collectPoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.collectPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

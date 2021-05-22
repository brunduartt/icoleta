import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { IColetaTestModule } from '../../../test.module';
import { CollectPointUpdateComponent } from 'app/entities/collect-point/collect-point-update.component';
import { CollectPointService } from 'app/entities/collect-point/collect-point.service';
import { CollectPoint } from 'app/shared/model/collect-point.model';

describe('Component Tests', () => {
  describe('CollectPoint Management Update Component', () => {
    let comp: CollectPointUpdateComponent;
    let fixture: ComponentFixture<CollectPointUpdateComponent>;
    let service: CollectPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IColetaTestModule],
        declarations: [CollectPointUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CollectPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CollectPointUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CollectPointService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CollectPoint(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CollectPoint();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

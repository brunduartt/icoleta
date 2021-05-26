import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { IMaterial, Material } from 'app/shared/model/material.model';
import { MaterialService } from 'app/entities/material/material.service';
import { FormControl } from '@angular/forms';
import { MaterialType } from 'app/shared/model/enumerations/material-type.model';

@Component({
  selector: 'jhi-material-selector',
  templateUrl: './material-selector.component.html',
  styleUrls: ['../collect-point.scss']
})
export class MaterialSelectorComponent implements OnInit {
  materials: IMaterial[] = [];
  MaterialType = MaterialType;
  @Input() formControl: FormControl | undefined;
  @Input() initAllChecked = false;
  @Output() changedSelect = new EventEmitter<null>();
  constructor(protected materialService: MaterialService) {}

  ngOnInit(): void {
    this.materialService.query().subscribe((res: HttpResponse<IMaterial[]>) => {
      if (this.initAllChecked && res.body) {
        this.materials = res.body.map(m => {
          m.checked = true;
          return m;
        });
      } else {
        this.materials = res.body || [];
      }
      if (this.formControl) {
        this.formControl.valueChanges.subscribe((ids: any) => {
          console.log(ids);
          this.updateForm(ids);
        });
        if (this.formControl.value) {
          this.updateForm(this.formControl.value);
        }
      }
    });
  }

  getCheckedMaterials(): IMaterial[] {
    return this.materials.filter(m => m.checked);
  }

  updateForm(checkedMaterialsIds: number[]): void {
    if (checkedMaterialsIds) {
      checkedMaterialsIds.forEach(id => {
        const index = this.materials.findIndex(m => m.id === id);
        if (index >= 0) {
          this.materials[index].checked = true;
        }
      });
    }
  }

  getMaterialTypeClass(materialOption: IMaterial): any {
    return (
      'material-option material-type-' +
      materialOption.materialType!.toString().toLowerCase() +
      ' ' +
      (materialOption.checked ? 'checked' : 'not-checked')
    );
  }

  addRemoveMaterial(material: Material): void {
    if (!this.formControl || this.formControl.enabled) {
      material.checked = !material.checked;
      this.changedSelect.emit();
    }
  }
}

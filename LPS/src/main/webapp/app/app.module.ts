import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { IColetaSharedModule } from 'app/shared/shared.module';
import { IColetaCoreModule } from 'app/core/core.module';
import { IColetaAppRoutingModule } from './app-routing.module';
import { IColetaHomeModule } from './home/home.module';
import { IColetaEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    IColetaSharedModule,
    IColetaCoreModule,
    IColetaHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    IColetaEntityModule,
    IColetaAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class IColetaAppModule {}

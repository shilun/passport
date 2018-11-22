import {Component, OnInit} from '@angular/core';
import {AbstractController} from "../../../common/abstract.controller";
import {GlobalService} from "../../../services/global.service";
import {ActivatedRoute, Router} from "@angular/router";
import {SoftwareService} from "../../../services/software.service";

@Component({
  selector: 'app-software-list',
  templateUrl: './software-list.component.html',
  styleUrls: ['./software-list.component.scss']
})
export class SoftwareListComponent extends AbstractController implements OnInit {
  statuses: any;
  proxyes: Array<any>;
  agenttypes: any;
  versiontypes: any;

  constructor(protected serverService: SoftwareService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(serverService, route, router);

    this.entity = {};
  }

  async ngOnInit() {
    await this.list(this.entity);
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('proxy');
    if (result.success) {
      this.proxyes = result.data.list;
    }
    result = await this.globalService.list('agenttype');
    if (result.success) {
      this.agenttypes = result.data.list;
    }
    result = await this.globalService.list('versiontype');
    if (result.success) {
      this.versiontypes = result.data.list;
    }

  }

}

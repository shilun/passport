import { Component, OnInit } from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {RoleService} from '../../../services/role.service';
import {ProxyService} from '../../../services/proxy.service';

@Component({
  selector: 'app-proxy-list',
  templateUrl: './proxy-list.component.html',
  styleUrls: ['./proxy-list.component.scss']
})
export class ProxyListComponent  extends AbstractController implements OnInit {

  statuses: Array<any>;

  constructor(protected proxyService: ProxyService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(proxyService, route, router);
    this.entity = {};
  }

  async ngOnInit() {
    this.list(this.entity);
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
  }

}


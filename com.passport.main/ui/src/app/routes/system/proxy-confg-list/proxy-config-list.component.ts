import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyService} from '../../../services/proxy.service';
import {ProxyUserService} from "../../../services/proxy.user.service";
import {ProxyconfigService} from "../../../services/proxyconfig.service";

@Component({
  selector: 'app-proxy-list',
  templateUrl: './proxy-config-list.component.html'
})
export class ProxyConfigListComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  proxyes: Array<any>;

  constructor(protected proxyService: ProxyconfigService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(proxyService, route, router);
    this.entity = {};
  }

  async ngOnInit() {
    this.entity.proxyId = this.getRequest("proxyId");
    this.list(this.entity);
    let result = await this.globalService.list('proxy');
    if (result.success) {
      this.proxyes = result.data.list;
    }
  }

}


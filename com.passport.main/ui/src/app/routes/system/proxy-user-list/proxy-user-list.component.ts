import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyService} from '../../../services/proxy.service';
import {ProxyUserService} from "../../../services/proxy.user.service";

@Component({
  selector: 'app-proxy-list',
  templateUrl: './proxy-user-list.component.html'
})
export class ProxyUserListComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  proxyes: Array<any>;

  constructor(protected proxyService: ProxyUserService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(proxyService, route, router);
    this.entity = {};
  }


  async ngOnInit() {
    this.entity.proxyId=this.getRequest("proxyId");
    this.list(this.entity);
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('proxy');
    if (result.success) {
      this.proxyes = result.data.list;
    }
  }

}


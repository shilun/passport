import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyService} from '../../../services/proxy.service';
import {ProxyBizService} from '../../../services/proxy.biz.service';

@Component({
  selector: 'app-proxy-biz-list',
  templateUrl: './proxy-biz-list.component.html'
})
export class ProxyBizListComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  gametypes: Array<any>;
  proxys: Array<any>;
  proxyId: any;

  constructor(protected proxyBizService: ProxyBizService, protected proxyService: ProxyService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(proxyBizService, route, router);
    this.entity = {};
  }

  /**
   * 获取代理商代理的游戏
   * @param value
   * @param list
   */
  gametypeParse(value: any, list) {
    try {
      if (value == null) {
        return '无';
      }
      for (let item of list) {
        if (item.value == value) {
          return item.name;
        }
      }
    }
    catch (e) {
    }
    return '未知';
  }

  async ngOnInit() {
    this.proxyId = this.getRequest('proxyId');
    this.entity.proxyId = this.proxyId;
    this.list(this.entity);
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('proxygame');
    if (result.success) {
      this.gametypes = result.data.list;
    }
    result = await this.proxyService.all();
    if (result.success) {
      let arr = [{id: null, name: '全部'}];
      for (let i = 0; i < result.data.list.length; i++) {
        arr.push(result.data.list[i]);
      }
      this.proxys = arr;
    }
  }

}


import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyService} from '../../../services/proxy.service';

@Component({
  selector: 'app-proxy-list',
  templateUrl: './proxy-list.component.html'
})
export class ProxyListComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  gametypes: Array<any>;

  constructor(protected proxyService: ProxyService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(proxyService, route, router);
    this.entity = {};
  }

  /**
   * 获取代理商代理的游戏
   * @param value
   * @param list
   */
  gametypeParse(value: any, list) {
    try {
      if (value == null || value.length == 0) {
        return '无';
      }
      let games = [];
      for (let item of list) {
        for (let game of value) {
          if (game == item.value) {
            games.push(item.name);
          }
        }
      }
      if (games.length != 0) {
        return games.join(',');
      } else {
        return '无';
      }
    }
    catch (e) {
    }
    return '未知';
  }

  async ngOnInit() {
    this.list(this.entity);
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('proxygame');
    if (result.success) {
      this.gametypes = result.data.list;
    }
  }

}


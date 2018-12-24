import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {ServerService} from '../../../services/server.service';

@Component({
  selector: 'app-server-list',
  templateUrl: './server-list.component.html'
})
export class ServerListComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  gametypes: Array<any>;
  proxyes: Array<any>;
  constructor(protected serverService: ServerService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(serverService, route, router);
    this.entity = {};
  }



  async ngOnInit() {
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

}


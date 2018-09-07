import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AbstractController} from '../../../common/abstract.controller';
import {UserService} from '../../../services/user.service';
import {GlobalService} from '../../../services/global.service';
import {ProxyService} from '../../../services/proxy.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html'
})
export class UserListComponent extends AbstractController implements OnInit {
  statuses: any;
  sexTypes: any;
  proxys: Array<any>;

  constructor(protected userService: UserService, protected proxyService: ProxyService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(userService, route, router);
    this.entity = {};
  }


  async ngOnInit() {
    this.list(this.entity);
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('sextype');
    if (result.success) {
      this.sexTypes = result.data.list;
    }
    result = await this.proxyService.list({pageinfo: {page: 0, size: 1000}})
    if (result.success) {
      let arr=[{id:null,name:'全部'}];
      for(let i=0;i<result.data.list.length;i++){
        arr.push(result.data.list[i]);
      }
      this.proxys=arr;
    }
  }

}

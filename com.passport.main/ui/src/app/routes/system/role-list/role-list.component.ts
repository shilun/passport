import {Component, OnInit} from '@angular/core';
import {AbstractController} from '../../../common/abstract.controller';
import {GlobalService} from '../../../services/global.service';
import {ActivatedRoute, Router} from '@angular/router';
import {RoleService} from '../../../services/role.service';

@Component({
  selector: 'app-role-list',
  templateUrl: './role-list.component.html'
})
export class RoleListComponent extends AbstractController implements OnInit {

  statuses: Array<any>;

  constructor(protected roleService: RoleService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(roleService, route, router);
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

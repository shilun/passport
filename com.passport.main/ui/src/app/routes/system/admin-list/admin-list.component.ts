import {Component, OnInit} from '@angular/core';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {AdminService} from '../../../services/admin.service';
import {GlobalService} from '../../../services/global.service';

@Component({
  selector: 'app-admin-list',
  templateUrl: './admin-list.component.html'
})
export class AdminListComponent extends AbstractController implements OnInit {

  statuses: Array<any>;

  constructor(protected adminService: AdminService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(adminService, route, router);
    this.entity = {};
  }

  async ngOnInit() {
    await this.list(this.entity);
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
  }

}

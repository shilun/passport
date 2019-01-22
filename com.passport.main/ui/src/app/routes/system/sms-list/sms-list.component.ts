import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {ServerService} from '../../../services/server.service';
import {SmsService} from "../../../services/sms.service";

@Component({
  selector: 'app-server-list',
  templateUrl: './sms-list.component.html'
})
export class SmsListComponent extends AbstractController implements OnInit {

  constructor(protected serverService: SmsService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(serverService, route, router);
    this.entity = {};
  }

  async ngOnInit() {
    this.list(this.entity);
  }
}


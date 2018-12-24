import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ServerService} from '../../../services/server.service';
declare let laydate;
@Component({
  selector: 'app-server-view',
  templateUrl: './server-view.component.html'
})
export class ServerViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  games: Array<any>;
  proxyes: Array<any>;
  constructor(fm: FormBuilder, baseService: ServerService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {id:'',gameType: '',gameId:'',status:'', serverId: '', ip: '', port: '', evironment: ''};
    this.valForm = this.buildFormGroup(fm);
  }


  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'ip': [null],
      'port': [null],
      'evironment': [null],
      'serverId': [null, Validators.required],
      'gameType': [null, Validators.required],
      'gameId': [null, Validators.required],
      'status': [null, Validators.required]
    });
  }

  async ngOnInit() {
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('proxy');
    if (result.success) {
      this.proxyes = result.data.list;
    }
    await this.viewById();


  }

  public save() {
    this.saveData('/system/server/list');
  }

  submitForm($ev, value: any) {
    this.save();
  }
}

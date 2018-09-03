import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyBizService} from '../../../services/proxy.biz.service';

@Component({
  selector: 'app-proxy-biz-view',
  templateUrl: './proxy-biz-view.component.html'
})
export class ProxyBizViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  bizTypes:Array<any>;

  constructor(fm: FormBuilder, baseService: ProxyBizService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {pin: '', bizType: '', status: '', id: '', startTime: '', endTime: '', proxyId: ''};
    this.valForm = this.buildFormGroup(fm);
  }


  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'bizType': [null, Validators.required],
      'domain': [null],
      'remark': [null],
      'status': [null, Validators.required],
      'startTime': [null, Validators.required],
      'endTime': [null, Validators.required]
    });
  }

  async ngOnInit() {
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    await this.viewById();
  }

  public save() {
    this.saveData('/system/proxyBiz/list');
  }

  submitForm($ev, value: any) {
    this.save();
  }
}

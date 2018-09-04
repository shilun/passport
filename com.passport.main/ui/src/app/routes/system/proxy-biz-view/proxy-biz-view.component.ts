import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyBizService} from '../../../services/proxy.biz.service';

declare let laydate;

@Component({
  selector: 'app-proxy-biz-view',
  templateUrl: './proxy-biz-view.component.html'
})
export class ProxyBizViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  bizTypes: Array<any>;

  constructor(fm: FormBuilder, baseService: ProxyBizService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {bizType: '', status: '', id: '', startTime: '', endTime: '', proxyId: ''};
    this.valForm = this.buildFormGroup(fm);
  }


  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'bizType': [null, Validators.required],
      'status': [null, Validators.required],
      'startTime': [null, Validators.required],
      'endTime': [null, Validators.required]
    });
  }

  async ngOnInit() {
    var _proxyId = this.getRequest('proxyId');
    if (_proxyId) {
      this.entity.proxyId = _proxyId;
    }
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('proxygame');
    if (result.success) {
      this.bizTypes = result.data.list;
    }
    await this.viewById();
    let start = laydate.render({
      elem: '#startTime', // s为页面日期选择输入框的id
      theme: '#0c6acf',
      isInitValue: false,
      done: (value, date, endDate) => {
        this.entity.startTime = value;
        end.config.min = {
          year: date.year,
          month: date.month - 1, //关键
          date: date.date,
        };
      }
    });
    let end = laydate.render({
      elem: '#endTime', // s为页面日期选择输入框的id
      theme: '#0c6acf',
      isInitValue: false,
      done: (value, date, endDate) => {
        this.entity.endTime = value;
        start.config.max = {
          year: date.year,
          month: date.month - 1, //关键
          date: date.date,
        };
      }
    });
  }

  public save() {
    this.saveData('/system/proxyBiz/list');
  }

  public async saveData(successUrl: string): Promise<void> {
    let result = await this.baseService.save(this.entity);
    if (result.success) {
      this.router.navigate([successUrl], {queryParams: {proxyId: this.entity.proxyId}});
    }
    else {
      alert(result.message);
    }
  }

  submitForm($ev, value: any) {
    this.save();
  }
}

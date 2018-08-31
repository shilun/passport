import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../../services/user.service';
import {ProxyService} from '../../../services/proxy.service';
declare let laydate;

@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html'
})
export class UserViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  sexTypes: Array<any>;
  proxys: Array<any>;

  constructor(fm: FormBuilder, baseService: UserService, protected proxyService: ProxyService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {pin: '', proxyId: '', nickName: '',  sexType: '', status: '', phone: '', id: '', birthday: '',email: ''};
    this.valForm = this.buildFormGroup(fm);
  }


  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'pin': [null],
      'proxyId': [null, Validators.required],
      'nickName': [null, Validators.required],
      'status': [null, Validators.required],
      'sexType': [null, Validators.required],
      'phone': [null, Validators.required],
      'email': [null],
      'birthday': [null]
    });
  }

  async ngOnInit() {
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('sextype');
    if (result.success) {
      this.sexTypes = result.data.list;
    }
    result = await this.proxyService.list({pageinfo: {page: 0, size: 1000}});
    if (result.success) {
      this.proxys = result.data.list;
    }
    await this.viewById();

    for (let proxyItem of this.proxys) {
      proxyItem.selected = '';
      let proxyId = this.entity.proxyId;
      if (proxyItem.id = proxyId) {
        proxyItem.checked = 'selected';
        break;
      }
    }

    laydate.render({
      elem:'#birthday', // s为页面日期选择输入框的id
      theme:'#0c6acf',
      isInitValue: false,
    done:(value, date, endDate)=>{
        this.entity.birthday = value;
      }
    })


  }

  public save() {
    this.saveData('/system/user/list');
  }

  submitForm($ev, value: any) {
    this.save();
  }
}

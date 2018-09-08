import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyService} from '../../../services/proxy.service';
declare let laydate;
@Component({
  selector: 'app-proxy-view',
  templateUrl: './proxy-view.component.html'
})
export class ProxyViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  games: Array<any>;

  constructor(fm: FormBuilder, baseService: ProxyService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {pin: '', name: '', status: '', phone: '', id: '', domain: '', linkMan: '',games:[], remark: '',endTime:''};
    this.valForm = this.buildFormGroup(fm);
  }


  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'name': [null, Validators.required],
      'pin': [null,Validators.required],
      'endTime': [null,Validators.required],
      'domain': [null],
      'remark': [null],
      'status': [null, Validators.required],
      'phone': [null, Validators.required],
      'linkMan': [null, Validators.required]
    });
  }

  async ngOnInit() {
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('games');
    if (result.success) {
      this.games = result.data.list;
    }
    await this.viewById();

    for (let gameItem of this.games) {
      gameItem.checked = '';
      for (let role of this.entity.games) {
        if (gameItem.value == role) {
          gameItem.checked = 'checked';
          break;
        }
      }
    }
    laydate.render({
      elem: '#endTime', // s为页面日期选择输入框的id
      theme: '#0c6acf',
      isInitValue: false,
      done: (value, date, endDate) => {
        this.entity.endTime = value;
      }
    });
  }

  upChecket(e: any, checket: boolean) {
    for (let item of this.games) {
      if (e.value == item.value) {
        if (checket) {
          item.checked = 'checked';
        }
        else {
          item.checked = '';
        }
        break;
      }
    }
  }
  public save() {
    this.saveData('/system/proxy/list');
  }

  submitForm($ev, value: any) {
    let games = [];
    for (let gameItem of this.games) {
      if (gameItem.checked != '') {
        games.push(gameItem.value);
      }
    }
    this.entity.endTime=this.entity.endTime.replace("/\//g","-");
    this.entity.games=games;
    this.save();
  }
}

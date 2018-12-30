import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyService} from '../../../services/proxy.service';
import {ProxyUserService} from "../../../services/proxy.user.service";
import {ProxyconfigService} from "../../../services/proxyconfig.service";
@Component({
  selector: 'app-proxy-view',
  templateUrl: './proxy-config-view.component.html'
})
export class ProxyConfigViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  proxyes: Array<any>;

  constructor(fm: FormBuilder, baseService: ProxyconfigService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {id:'',proxyId:'',content:''};
    this.valForm = this.buildFormGroup(fm);
  }


  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'proxyId': [null,Validators.required],
      'id': [null],
      'content': [null, Validators.required]
    });
  }

  async ngOnInit() {

    this.entity.proxyId=this.getRequest("proxyId");
    if (this.getRequest('id') != ''&&this.getRequest('id')!=undefined) {
     await this.viewById();
    }
    else{
      this.entity.id=null;
    }
    let result = await this.globalService.list('proxy');
    if (result.success) {
      this.proxyes = result.data.list;
    }

  }

  public save() {
    this.saveData('');
  }

  public async saveData(successUrl: string): Promise<void> {
    let result = await this.baseService.save(this.entity);
    if (result.success) {
      this.router.navigate(["/system/proxyconfig/list"]);
    }
    else {
      alert(result.message);
    }
  }
  submitForm($ev, value: any) {
    this.save();
  }
}

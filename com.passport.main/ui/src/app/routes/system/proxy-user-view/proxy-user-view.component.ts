import {Component, OnInit} from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ProxyService} from '../../../services/proxy.service';
import {ProxyUserService} from "../../../services/proxy.user.service";
@Component({
  selector: 'app-proxy-view',
  templateUrl: './proxy-user-view.component.html'
})
export class ProxyUserViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  proxyes: Array<any>;

  constructor(fm: FormBuilder, baseService: ProxyUserService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {proxyId:'',pin: '', status: '', phone: '', id: '',  desc: '',roles:''};
    this.valForm = this.buildFormGroup(fm);
  }


  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'pin': [null,Validators.required],
      'phone': [null],
      'desc': [null],
      'roles': [null],
      'id': [null],
      'status': [null, Validators.required]
    });
  }

  async ngOnInit() {

    this.entity.proxyId=this.getRequest("proxyId");
    if (this.getRequest('id') != ''&&this.getRequest('id')!=undefined) {
     await this.viewById();
      let roles="";
      for(let i in this.entity.roles){
        roles=roles+","+this.entity.roles[i];
      }
      roles=roles.substr(1);
      this.entity.roles=roles;
    }
    else{
      this.entity.id=null;
    }
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('proxy');
    if (result.success) {
      this.proxyes = result.data.list;
    }

  }

  public save() {
    this.entity.roles=this.entity.roles.split(",");
    this.saveData('');
  }

  public async saveData(successUrl: string): Promise<void> {
    let result = await this.baseService.save(this.entity);
    if (result.success) {
      this.router.navigate(["/system/proxyuser/list"],{queryParams:{proxyId:this.entity.proxyId}});
    }
    else {
      alert(result.message);
    }
  }
  submitForm($ev, value: any) {
    this.save();
  }
}

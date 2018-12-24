import {Component, OnInit} from '@angular/core';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AdminService} from '../../../services/admin.service';
import {GlobalService} from '../../../services/global.service';
import {RoleService} from '../../../services/role.service';
import {ProxyUserService} from "../../../services/proxy.user.service";

@Component({
  selector: 'app-admin-view',
  templateUrl: './proxy-user-pass.component.html'
})
export class ProxyUserPassComponent extends AbstractController implements OnInit {

  proxyId:any;

  constructor(fm: FormBuilder, protected baseService: ProxyUserService, protected roleService: RoleService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {id: '',pin:'',proxyId:""};
    this.entity.vpassword="";
    this.entity.password="";
    this.valForm = this.buildFormGroup(fm);

  }



  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'id': [null],
      'vpassword': [null, Validators.required],
      'password': [null, Validators.required]
    });
  }

  async ngOnInit() {
    await this.viewById();
    this.proxyId=this.entity.proxyId;
    this.entity={id:this.entity.id,pin:this.entity.pin};
    this.entity.vpassword="";
    this.entity.password="";
    this.proxyId=this.entity.proxyId;
  }

  public save() {
    if(this.entity.password!=this.entity.vpassword){
      alert("密码不一至");
      return ;
    }
    this.saveData('');
  }

  /**
   * 保存
   * @param {string} successUrl 保存成功跳转url
   */
  public async saveData(successUrl: string): Promise<void> {
    let result = await this.baseService.changePass(this.entity.id,this.entity.password);
    if (result.success) {
      this.router.navigate(["/system/proxyuser/list"],{queryParams:{proxyId:this.proxyId}});
    }
    else {
      alert(result.message);
    }
  }

  submitForm($ev, value: any) {
    this.save();
  }
}

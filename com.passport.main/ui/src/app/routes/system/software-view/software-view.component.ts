import { Component, OnInit } from '@angular/core';
import {AbstractController} from "../../../common/abstract.controller";
import {SoftwareService} from "../../../services/software.service";
import {GlobalService} from "../../../services/global.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {FileUploader} from "ng2-file-upload";

@Component({
  selector: 'app-software-view',
  templateUrl: './software-view.component.html',
  styleUrls: ['./software-view.component.scss']
})
export class SoftwareViewComponent extends AbstractController implements OnInit {
  statuses: any;
  proxyes: Array<any>;
  agenttypes:any;
  versiontypes:any;
  uploader: FileUploader = new FileUploader({
    url: '/api/software/file',
    method: "POST",
    itemAlias: "file",
    autoUpload: true
  });
  constructor(fm: FormBuilder,protected serverService: SoftwareService, protected globalService: GlobalService, protected route: ActivatedRoute, protected router: Router) {
    super(serverService, route, router);
    this.entity = {id:'',proxyId: '',name:'',status:'', versionType: '', mandatory: '', version: '', appSign: '',url:'',osType:''};
    this.valForm = this.buildFormGroup(fm);
    var page = this;
    this.uploader.onSuccessItem = function (fileItem, response, status, headers) {
      let result = JSON.parse(response);
      if (result.success) {
        page.entity.url = result.data;
      }
      else {
        alert(result.message);
      }
    };
  }

  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'id': [null],
      'proxyId': [null, Validators.required],
      'name': [null, Validators.required],
      'status': [null, Validators.required],
      'versionType': [null, Validators.required],
      'mandatory': [null, Validators.required],
      'version': [null, Validators.required],
      'appSign': [null, Validators.required],
      'url': [null],
      'osType': [null, Validators.required]
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
    result = await this.globalService.list('agenttype');
    if (result.success) {
      this.agenttypes = result.data.list;
    }
    result = await this.globalService.list('versiontype');
    if (result.success) {
      this.versiontypes = result.data.list;
    }
    this.viewById();

  }

  uploadFileHandel() {
    this.uploader.queue[0].onSuccess = function (response, status, headers) {
      // 上传文件成功
      if (status == 200) {
        // 上传文件后获取服务器返回的数据
        let tempRes = JSON.parse(response);
      } else {
        // 上传文件后获取服务器返回的数据错误
      }
    };
    this.uploader.queue[0].upload(); // 开始上传
  }
  public save() {
    this.saveData('/system/software/list');
  }

  submitForm($ev, value: any) {
    this.save();
  }
}

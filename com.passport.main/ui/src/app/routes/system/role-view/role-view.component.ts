import {Component, OnInit} from '@angular/core';
import {AbstractController} from '../../../common/abstract.controller';
import {GlobalService} from '../../../services/global.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RoleService} from '../../../services/role.service';

@Component({
  selector: 'app-role-view',
  templateUrl: './role-view.component.html'
})
export class RoleViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  constructor(fm: FormBuilder, baseService: RoleService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {name: '', status: '', remark: '', resources: '', id: ''};
    this.valForm = this.buildFormGroup(fm);

  }


  public buildFormGroup(fb: FormBuilder): FormGroup {

    return fb.group({
      'name': [null, Validators.required],
      'status': [null, Validators.required],
      'remark': [null, Validators.required],
      'resources': [null, Validators.required],
    });
  }

  async ngOnInit() {
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    this.viewById();
  }

  public save() {
    this.saveData('/system/role/list');
  }

  submitForm($ev, value: any) {
    this.save();
  }
}

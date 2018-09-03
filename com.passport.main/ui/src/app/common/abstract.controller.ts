import 'rxjs/add/operator/toPromise';


import {AbstractBaseService} from './abstract-base.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, ValidatorFn} from '@angular/forms';
import {StringUtils} from './string.utils';


/**
 * 页面抽像对象
 */

export abstract class AbstractController {

  public listData: any = [];

  public entity: any;
  public pageSize: number = 10;
  public pageIndex: number = 0;
  public actionType: string = '添加';

  public valForm: FormGroup;


  constructor(protected baseService: AbstractBaseService, protected route: ActivatedRoute, protected router: Router) {
    this.entity = {};
  }


  /**
   * 获取请求值
   * @param {string} name
   * @returns {any}
   */
  protected getRequest(name: string): any {
    return this.route.snapshot.queryParams[name];
  }


  /**
   * 绑定查看
   */
  public async viewById(): Promise<void> {
    var id = this.getRequest('id');
    if (!StringUtils.isBlank(id)) {
      this.actionType = '修改';
      let result = await this.baseService.view(id);
      if (result.success) {
        let keys = Object.keys(this.entity);
        for (let key of keys) {
          this.entity[key] = result.data[key];
        }
      }
      else {
        console.error(result.message);
      }
    }
  }

  /**
   * 保存
   * @param {string} successUrl 保存成功跳转url
   */
  public async saveData(successUrl: string): Promise<void> {
    let result = await this.baseService.save(this.entity);
    if (result.success) {
      this.router.navigate(successUrl.split('/'));
    }
    else {
      alert(result.message);
    }
  }

  /**
   * 查看列表
   */
  public async list(query): Promise<void> {
    if (query == null) {
      query = {};
    }

    let keys = Object.keys(query);
    let haspage = false;
    for (let key of keys) {
      if (key == 'pageInfo') {
        haspage = true;
        break;
      }
    }
    if (!haspage) {
      query = {...query, pageinfo: {page: 0, size: this.pageSize}};
    }
    let result = await this.baseService.list(query);
    if (result.success) {
      this.listData = result.data;
    }
    else {
      console.error(result.message);
    }
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  /**
   * 切换页面
   * @param $event
   * @returns {any}
   */
  public async onPageChange($event): Promise<any> {
    this.pageIndex = $event.page;
    this.pageIndex = this.pageIndex - 1;
    let result = await this.baseService.list({...this.entity, pageinfo: {size: this.pageSize, page: $event.page - 1}});
    if (result.success) {
      this.listData = result.data;
    }
  }

  /**
   * 值名称
   * @param value
   * @param list
   * @returns {any}
   */
  valuePare(value: any, list) {
    try {
      for (let item of list) {
        if (item.value == value) {
          return item.name;
        }
      }
    }
    catch (e) {
    }
    return '未知';
  }

  /**
   * 值名称
   * @param value
   * @param list
   * @returns {any}
   */
  proxyParse(value: any, list) {
    try {
      if (value == null || value == 0) {
        return '';
      }
      for (let item of list) {
        if (item.id == value) {
          return item.name;
        }
      }
    }
    catch (e) {
    }
    return '未知';
  }

  /**
   * 验证最小字符
   * @param checkValue
   * @returns {ValidatorFn}
   */
  protected validatorMinWords(checkValue): ValidatorFn {
    return <ValidatorFn>((control: FormControl) => {
      return (control.value || '').split(' ').length >= checkValue ? null : {'minWords': control.value};
    });
  }

  /**
   * 验证最大字符
   * @param checkValue
   * @returns {ValidatorFn}
   */
  protected validatorMaxWords(checkValue): ValidatorFn {
    return <ValidatorFn>((control: FormControl) => {
      return (control.value || '').split(' ').length <= checkValue ? null : {'maxWords': control.value};
    });
  }

  /**
   * 提交订单
   * @param $ev
   * @param value
   */
  submitForm($ev, value: any) {

  }
}






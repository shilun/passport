import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'actionType'
})
export class ActionType implements PipeTransform {

  transform(value: any, args?: any): any {
    if(value==1){
      return "买入";
    }
    if(value==2){
      return "卖出";
    }
  }

}

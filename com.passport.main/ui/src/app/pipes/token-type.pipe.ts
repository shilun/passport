import {Pipe, PipeTransform} from '@angular/core';
import {TokenTypeEnum} from '../modules/TokenTypeEnum';

@Pipe({
  name: 'tokenType'
})
export class TokenTypePipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return TokenTypeEnum[value];
  }

}

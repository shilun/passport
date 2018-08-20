import {Pipe, PipeTransform} from '@angular/core';
import {Certification} from '../modules/certification.enum';

@Pipe({
  name: 'certification'
})
export class CertificationPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return Certification[value];
  }

}

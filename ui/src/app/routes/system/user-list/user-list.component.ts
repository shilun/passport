import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AbstractController} from '../../../common/abstract.controller';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html'
})
export class UserListComponent extends AbstractController implements OnInit {

  constructor(protected userService: UserService, protected route: ActivatedRoute, protected router: Router) {
    super(userService, route, router);
    this.entity = {id: '', pin: '', nickName: '', phone: '', email: '', sexType: '', status: ''};
  }

  ngOnInit() {
    this.list(this.entity);
  }

}

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CookieService } from 'src/app/services/cookie.service';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  showPassword: boolean = false

  loginForm : FormGroup = new FormGroup({
    email: new FormControl(null, [
      Validators.required,
      Validators.email
    ]),
    password: new FormControl(null, [
      Validators.required,
      Validators.minLength(8),
    ]),
  })

  constructor(private authService: AuthService, private router: Router, private cookieService: CookieService) { }

  ngOnInit(): void {
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

    submitData(): void {
    this.loginForm.markAllAsTouched();

    if (this.loginForm.invalid) {
      return;
    }

    this.authService.login(this.loginForm.value).subscribe(
      (res) => { 
        if(res.code == 200){
          this.cookieService.set("token", res.token, 10);
          this.router.navigate(["/transactions"]);
        }else{
          alert(res.message);
        }
      },
      (err) => {
        console.error('Registration failed:', err);
        if (err.error?.message) {
          alert(err.error.message);
        } else {
          alert('login failed. Please try again.');
        }
      }
    );
  }

}

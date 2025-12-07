import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CookieService } from '../../../services/cookie.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

export class RegisterComponent implements OnInit {
  showPassword: boolean = false;

  registerForm: FormGroup = new FormGroup({
    name: new FormControl(null, [
      Validators.required, 
      Validators.minLength(3)
    ]),
    email: new FormControl(null, [
      Validators.required,
      Validators.email
    ]),
    password: new FormControl(null, [
      Validators.required,
      Validators.minLength(8),
    ]),
    phone: new FormControl(null, [
      Validators.required,
      Validators.minLength(10),
      Validators.maxLength(15)
    ]),
    address: new FormControl(null, [
      Validators.required
    ]),
  });

  constructor(
    private authService: AuthService,
    private cookieService: CookieService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  submitData(): void {
    this.registerForm.markAllAsTouched();

    if (this.registerForm.invalid) {
      return;
    }

    this.authService.register(this.registerForm.value).subscribe(
      (res) => {
        if(res.code == 201) {
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
          alert('Registration failed. Please try again.');
        }
      }
    );
  }
}
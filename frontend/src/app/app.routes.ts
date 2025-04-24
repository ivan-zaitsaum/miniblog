import { Routes } from '@angular/router';
import { AuthComponent } from './auth/auth.component';
import { PostsComponent } from './posts/posts.component';

export const routes: Routes = [
    { path: '', component: PostsComponent },
    { path: 'auth', component: AuthComponent }
];

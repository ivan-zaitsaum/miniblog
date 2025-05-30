import { Routes } from '@angular/router';
import { AuthComponent } from './auth/auth.component';
import { PostsComponent } from './posts/posts.component';
import { AddPostComponent } from './add-post/add-post.component';
import { EditPostComponent } from './edit-post/edit-post.component';
import { ProfileComponent } from './profile/profile.component';


export const routes: Routes = [
  { path: '', component: PostsComponent },
  { path: 'auth', component: AuthComponent },
  { path: 'add-post', component: AddPostComponent },
  { path: 'edit-post/:id', component: EditPostComponent },
  { path: 'profile', component: ProfileComponent },
];

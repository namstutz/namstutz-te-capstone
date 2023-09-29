import Vue from 'vue'
import Router from 'vue-router'
// import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import Register from '../views/Register.vue'
import store from '../store/index'
import Books from '../views/Books.vue'
import Family from '../views/Family.vue'
import Book from '../views/Book.vue'
import Activity from '../views/Activity.vue' 
import Prizes from '../views/Prizes.vue'


Vue.use(Router)

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      redirect: { name: 'books' } 
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: Logout,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/books",
      name: "books",
      component: Books,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/family",
      name: "family",
      component: Family, 
      meta: {
        requiresAuth: true,
        requiresParent: true
      }
    },
    {
      path: "/books/recommended",
      name: "bookRecommended",
      component: Book,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/books/:id",
      name: "book",
      component: Book,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/activity",
      name: "activity",
      component: Activity,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/prizes",
      name: "prizes",
      component: Prizes,
      meta: {
        requiresAuth: true
      }
    }
    
  ]
})

router.beforeEach((to, from, next) => {
  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    next("/login");
  } else {
    // Else let them go to their next destination
    next();
  }

  const requiresParent = to.matched.some(x => x.meta.requiresParent);
  if (requiresParent && store.state.user.authorities[0].name != 'ROLE_PARENT') {
    next("/books");
  } else {
    next();
  }
});

export default router;

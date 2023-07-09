import Vue from 'vue'
import VueRouter from 'vue-router'
import register from '@/views/Register'
import logon from '@/components/Logon'
import chatList from '@/views/ChatList'
import chatWindow from '@/views/ChatWindow'
import newContact from '@/views/NewContact'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'register',
    component: register
  },
  {
    path: '/logon',
    name: 'logon',
    component: logon
  },
  {
    path: '/chatList',
    name: 'chatList',
    component: chatList
  },
  {
    path: '/chatWindow',
    name: 'chatWindow',
    component: chatWindow
  },
  {
    path: '/newContact',
    name: 'newContact',
    component: newContact
  }
]

const router = new VueRouter({
  routes
})

export default router

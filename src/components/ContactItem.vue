<template>
  <div id="contactItem" :class="active?'active':'unActive'"
       @mouseover="selectStyle()" @mouseout="outStyle()" @click="newChat()">
    <img :src="circleUrl" class="avatars" ref="theAvatar">
    <h3 style="margin: 5% 0 0;">{{theInfor.name}}</h3>
    <h4 style="margin: 0">{{theInfor.account}}</h4>
  </div>
</template>

<script>
export default {
  name: 'ContactItem',
  data () {
    return {
      active: false,
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      user:{
        account:""
      }
    }
  },
  props: {
    theInfor: Object
  },
  mounted () {
    this.circleUrl = require('@/assets/uploadImg/' + this.theInfor.avatar)
  },
  methods: {
    selectStyle () {
      this.active = true
    },
    outStyle () {
      this.active = false
    },
    newChat () {
      this.getQueryVariable()
      let a = {
        user: this.user.account,
        contact: this.theInfor.account
      }
      this.$electron.ipcRenderer.send('new-chatWindow',a)
    },
    //获取url传参
    getQueryVariable() {
      const query = window.location.href
      const vars = query.split('?')
      const a  = vars[1].split(/=|&/)
      this.user.account = a[1]
    }
  }
}
</script>

<style scoped>
#contactItem {
  width: 100%;
  height: 75px;
  display: inline-block;
  /*border: black solid 1px;*/
}

.active{
  color: red;
  background: darkgray;
  cursor: pointer;
}

.unActive{
  color: black;
  background: white;
  cursor: default;
}

.avatars {
  width: 50px;
  height: 50px;
  float: left;
  margin-top: 5%;
  border: 1px dashed #d9d9d9;
  border-radius: 42px;
}
</style>

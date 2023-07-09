<template>
  <div id="inforItem" :class="active?'active':'unActive'"
       @mouseover="selectStyle()" @mouseout="outStyle()" @click="newChat()">
    <img :src="circleUrl" class="infor-avatars" ref="theAvatar">
    <div>
      <strong class="info-h3">{{name}}</strong>
      <div class="info-date">{{this.unreadMessage.date}}</div>
      <div ref="say" class="info-h4"></div>
      <el-badge :value="this.unreadMessage.count" class="item" :hidden="false">
      </el-badge>
    </div>
    <div style="clear: both"></div>
  </div>
</template>

<script>
export default {
  name: 'InforItem',
  data () {
    return {
      active: false,
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      name,
      contact: {
        account: "",
        name: "",
        avatar:""
      }
    }
  },
  props: {
    unreadMessage: Object,
    user:String
  },
  mounted () {
    this.getData()
    this.$refs.say.innerHTML = this.unreadMessage.say
  },
  methods: {
    selectStyle () {
      this.active = true
    },
    outStyle () {
      this.active = false
    },
    newChat () {
      let a = {
        user: this.user,
        contact: this.unreadMessage.account
      }
      this.$electron.ipcRenderer.send('new-chatWindow',a)
    },
    //获取user信息
    async getData() {
      const res = await this.$axios({
        url:"http://localhost:8866/login/searchContact",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account: this.unreadMessage.account,
        },
      }).then(res => {
        if (res.data !== '') {
          this.contact.name = res.data.name
          this.contact.avatar = res.data.avatar
          //获取头像
          if (this.contact.avatar == null || this.contact.avatar === '') {
            this.circleUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          }else {
            this.circleUrl = require('@/assets/uploadImg/' + this.contact.avatar)
          }
          //获取名字
          this.name = this.contact.name
        }
      })
    },
  }
}
</script>

<style scoped>
#inforItem {
  width: 100%;
  text-align: left;
  /*border: black solid 1px;*/
  height: 100px;
}

.active{
  background: darkgray;
  cursor: pointer;
}

.unActive{
  color: black;
  background: white;
  cursor: default;
}

.infor-avatars {
  width: 50px;
  height: 50px;
  float: left;
  margin-top: 7%;
  border: 1px dashed #d9d9d9;
  border-radius: 42px;
}

.item{
  float: right;
  top: -50px;
  margin: 0 20px 0 0;
}

.info-h3 {
  font-size: 20px;
  margin: 0;
}

.info-h4 {
  margin: 0;
}

.info-date{
  font-size: 10px;
  margin: 0;
}
</style>

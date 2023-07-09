<template>
  <div id="login">
    <el-input class="login-input" v-model="account" placeholder="请输入账号">
      <i slot="prefix" class="el-input__icon el-icon-chat-round"></i>
    </el-input>
    <el-input show-password class="login-input" v-model="password" placeholder="请输入密码">
      <i slot="prefix" class="el-input__icon el-icon-lock"></i>
    </el-input>
    <el-button type="primary" id="login-button" @keyup.enter.native="newWindow()" @click="newWindow()">登     录</el-button>
    <div class="msg" ref="msg">用户或密码不正确</div>
  </div>
</template>

<script>
export default {
  name: 'login',
  data () {
    return {
      account: '',
      password: '',
      user: {
        account: '',
        password: '',
        name: '',
        avatar: ''
      }
    }
  },
  mounted () {
    this.showAvatar()
  },
  methods: {
    newWindow () {
      this.getData()
    },
    showAvatar () {
      if (JSON.parse(localStorage.getItem('user')) != null) {
        this.user.avatar = JSON.parse(localStorage.getItem('user')).avatar
        this.user.account = JSON.parse(localStorage.getItem('user')).avatar
        this.$emit("avatarChanged",this.user.avatar);
      }
    },
    async getData() {
      const res = await this.$axios({
        url:"http://localhost:8866/login",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account: this.account,
          password: this.password
        },
      }).then(res => {
        if (res.data !== '') {
          this.user.account = res.data.account
          this.user.name = res.data.name
          this.user.avatar = res.data.avatar
          //存入数据到本地缓存，实现持久化
          localStorage.setItem("user",JSON.stringify(this.user))
          this.account = ''
          this.password = ''
          this.$refs.msg.style.display = 'none'
          this.$electron.ipcRenderer.send('new-window',this.user.account)
        }else {
          this.$refs.msg.style.display = 'block'
          this.password = '';
        }
      })
    }
  }
}
</script>

<style scoped>
#login {
  margin-top: 30px;
}

.login-input {
  -webkit-app-region: no-drag;
  width: 250px;
  margin-top: 10px;
}

#login-button {
  -webkit-app-region: no-drag;
  position: absolute;
  text-align: center;
  line-height: 0;
  height: 30px;
  width: 250px;
  top: 70%;
  left: 21%;
}

.msg {
  position: absolute;
  color: red;
  left: 35%;
  top: 85%;
  display: none;
}
</style>

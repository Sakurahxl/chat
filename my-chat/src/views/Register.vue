<template>
  <div id="register">
    <div id="my-chat">
      <img :src="logo" alt="logo" id="my-chat-icon">
      <p id="my-chat-name">MYChat</p>
    </div>
    <div id="window-operation">
      <el-tooltip class="item" effect="light" content="最小化" placement="bottom-start">
        <i class="el-icon-minus" @click="miniWin()"></i>
      </el-tooltip>
      <el-tooltip class="item" effect="light" content="关闭" placement="bottom-start">
        <i class="el-icon-close" @click="closeWin()"></i>
      </el-tooltip>
    </div>
    <div id="register-avatar" class="block"><el-avatar :size="75" :src="circleUrl"></el-avatar></div>
    <div id="window-top">
    </div>
    <div id="window-bottom">
      <Login v-on:avatarChanged="updateAvatar"/>
      <div id="register-logon">
        <el-tooltip class="item" effect="light" content="注册账号" placement="top-start">
          <a ref="logon" @click="openLogon()">注册账号</a>
        </el-tooltip>
      </div>
    </div>
  </div>
</template>

<script>
import Login from '@/components/Login'
export default {
  name: 'register',
  data () {
    return {
      logo: require('@/assets/desktop-icon.png'),
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  methods: {
    // 关闭窗口命令
    closeWin () {
      this.$electron.ipcRenderer.send('window-close')
    },

    // 最小化窗口命令
    miniWin () {
      this.$electron.ipcRenderer.send('window-mini')
    },

    //打开注册网页
    async openLogon () {
      const { shell } = window.require("electron")
      let href = "http://localhost:8081/#/logon"
      try {
        await shell.openExternal(href);
      }catch (e) {
        console.log('成功解决！ ')
      }
    },
    updateAvatar (e) {
      this.logo = require('@/assets/uploading' + e)
    }

  },
  components: { Login }
}
</script>

<style>
#my-chat {
  position: absolute;
  left: 5px;
  top: 5px;
  z-index: 100;
}

#my-chat-icon {
  width: 30px;
}

#my-chat-name {
  position: absolute;
  left: 40px;
  top: -10px;
  z-index: 100;
}

#register {
  width: 425px;
  height: 325px;
}

#window-operation {
  position: absolute;
  right: 0;
  margin-right: 10px;
  z-index: 100;
  cursor: pointer;
}

#window-top {
  position: absolute;
  height: 125px;
  width: 100%;
  background-image: url("../assets/login.jpg");
  background-size:100% 125px;
  background-repeat: no-repeat;
}

#window-bottom {
  position: absolute;
  top: 125px;
  width: 100%;
  height: 201px;
}

.block {
  position: absolute;
  z-index: 100;
}

#register-avatar {
  left: 40%;
  top: 25%;
}

#register-logon {
  -webkit-app-region: no-drag;
  position: absolute;
  top: 90%;
  cursor: pointer;
}

a{
  -webkit-app-region: no-drag;
  text-decoration: none;
  color: darkgray;
  font-size: 12px;
}

.el-icon-close {
  -webkit-app-region: no-drag;
}

.el-icon-minus {
  -webkit-app-region: no-drag;
}


.item {
  margin: 4px;

}
</style>

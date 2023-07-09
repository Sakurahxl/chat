<template>
  <div id="chatList">
    <div id="window-operation">
      <i title="最小化" class="el-icon-minus" @click="miniWin()"></i>
      <i title="关闭" class="el-icon-close" @click="closeWin()"></i>
    </div>
    <div class="modify-name" ref="chat-modify-name">
      <el-input placeholder="请输入内容" v-model="name" clearable @keyup.enter.native="confirmModify" @blur="confirmModify"></el-input>
    </div>
    <h1 class="user-name" v-text="name" @dblclick="modify()" ref="user-name"></h1>
    <el-button type="primary" icon="el-icon-plus" class="add-friend" size="mini" @click="addFriend"></el-button>
    <div id="chatList-avatar" class="block"><img :src="circleUrl" id="theAvatar" ref="theAvatar"></div>
    <el-upload
      class="avatar-uploader"
      action="http://localhost:8866/login/updateAvatar"
      name="avatar"
      :data="otherData"
      :multiple="false"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      :headers=header>
      <img v-if="imageUrl" :src="imageUrl" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    <div id="chatList-main">
      <div id="chatList-select">
        <el-tabs v-model="activeName" :stretch='true'>
          <el-tab-pane label="消息" name="first"></el-tab-pane>
          <el-tab-pane label="联系人" name="second"></el-tab-pane>
        </el-tabs>
      </div>
      <div v-if="activeName==='first'">
        <Information :user="user"/>
      </div>
      <div v-if="activeName==='second'">
        <Contacts :user="user"/>
      </div>
    </div>
  </div>
</template>

<script>
import Information from '@/components/Information'
import Contacts from '@/components/Contacts'

export default {
  name: 'chatList',
  components: { Contacts, Information },
  data () {
    return {
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      activeName: 'first',
      name: '',
      user: {
        account:"",
        name:"",
        avatar:""
      },
      //上传图片地址
      imageUrl: '',
      header: {
        token: window.localStorage['token']
      },
      otherData:{
        account:''
      },
    }
  },
  mounted : function() {
    this.getQueryVariable();
  },
  methods: {
    // 关闭窗口命令
    closeWin () {
      const win = this.$remote.getCurrentWindow()
      win.close()
    },
    // 最小化窗口命令
    miniWin () {
      const win = this.$remote.getCurrentWindow()
      win.minimize()
    },
    //添加好友
    addFriend() {
      // this.setLocal()
      this.getQueryVariable()
      this.$electron.ipcRenderer.send('new-contactWindow',this.user.account)
    },
    //修改用户名
    modify() {
      this.$refs['user-name'].style.display = 'none'
      //获取不到样式，设置页面加载完成后执行
      this.$nextTick(()=>{
        this.$refs['chat-modify-name'].style.display = 'block'
      })
    },
    //确认修改
    confirmModify() {
      this.$refs['user-name'].style.display = 'block'
      this.$refs['chat-modify-name'].style.display = 'none'
      this.setNameData();
    },
    async setNameData() {
      this.getQueryVariable()
      const res = await this.$axios({
        url:"http://localhost:8866/login/updateName",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account: this.user.account,
          name: this.name
        },
      }).then(res => {
        if (res.data !== '') {
          console.log(res.data)
        }else {
          console.log("error")
        }
      })
    },
    //上传
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
      this.circleUrl = this.imageUrl
    },

    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },

    //获取user信息
    async getData() {
      const res = await this.$axios({
        url:"http://localhost:8866/login/searchContact",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account: this.user.account,
        },
      }).then(res => {
        if (res.data !== '') {
          this.user.account = res.data.account
          this.user.name = res.data.name
          this.user.avatar = res.data.avatar
          //获取头像
          if (this.user.avatar == null || this.user.avatar === '') {
            this.circleUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          }else {
            this.circleUrl = require('@/assets/uploadImg/' + this.user.avatar)
          }
          //获取名字
          this.name = this.user.name
          this.otherData.account = this.user.account
        }
      })
    },
    //获取url传参
    getQueryVariable() {
      const query = window.location.href
      const vars = query.split('?')
      const a  = vars[1].split(/=|&/)
      this.user.account = a[1]
      this.getData()
    }
  }
}
</script>

<style scoped>
#chatList {
  height: 550px;
  background-image: url("../assets/123.jpg");
  background-size:100% 120px;
  background-repeat: no-repeat;
}

#chatList-avatar {
  -webkit-app-region: no-drag;
  position: absolute;
  left: 5%;
  top: 5%;
}

#chatList-main {
  z-index: 100;
  position: absolute;
  width: 100%;
  height: 80%;
  top: 110px;
}

#chatList-select {
  -webkit-app-region: no-drag;
}

.add-friend {
  -webkit-app-region: no-drag;
  position: absolute;
  right: 0;
  top: 21.6%;
  z-index: 1000;
  background: none;
  color: rgb(161,164,166);
  border: none;
}

.add-friend:hover {
  background: rgb(241,241,241);
  border: none;
  color: rgb(161,164,166);
}

.user-name {
  text-align: left;
  width: 210px;
  -webkit-app-region: no-drag;
  position: absolute;
  left: 30%;
  cursor: pointer;
}

.modify-name {
  -webkit-app-region: no-drag;
  position: absolute;
  left: 30%;
  width: 210px;
  top: 4%;
  display: none;
}

/*上传*/
.avatar-uploader {
  -webkit-app-region: no-drag;
  opacity: 0;
  width: 75px;
  height: 75px;
  position: absolute;
  left: 5%;
  top: 5%;
  border: 1px dashed #d9d9d9;
  border-radius: 42px;
  cursor: pointer;
  overflow: hidden;
  z-index: 100;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

#theAvatar {
  width: 75px;
  height: 75px;
  position: absolute;
  left: 5%;
  top: 5%;
  border: 1px dashed #d9d9d9;
  border-radius: 42px;
}
</style>

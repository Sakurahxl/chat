<template>
  <div id="contacts">
    <div class="list-wrap" ref="listWrap">
      <div class="scroll-bar" ref="scrollBar"></div>
      <div class="list" ref="list">
        <ContactItem v-for="(val,index) in showList" :theInfor='val' :key='index'/>
      </div>
    </div>
  </div>
</template>

<script>
import ContactItem from '@/components/ContactItem'
export default {
  name: 'Contacts',
  components: { ContactItem },
  data () {
    return {
      contacts: null// 显示数据
    }
  },
  computed: {
    // 显示的数组，用计算属性计算
    showList () {
      return this.contacts
    }
  },
  props: {
    user: Object
  },
  mounted () {
    this.getUserList()
    // 构造滚动条高度
    this.$refs.scrollBar.style.height = '400px'
  },
  methods: {
    async getUserList () {
      const res = await this.$axios({
        url: "http://localhost:8866/login/userList",
        contentType: "application/x-www-form-urlencoded",
        params: {
          account: this.user.account,
        },
      }).then(res => {
        if (res.data !== "") {
          this.contacts = res.data
        } else {
          console.log("null")
        }
      })
    }
  }
}
</script>

<style scoped>
#contacts {
  -webkit-app-region: no-drag;
  position: absolute;
  top: 40px;
  width: 100%;
}

.list-wrap{
  position: relative;
  overflow-y: scroll;
  width: 100%;
  box-sizing: border-box;
}
.list{
  position: absolute;
  top: 2px;
  left: 0;
  width: 100%;
}
</style>

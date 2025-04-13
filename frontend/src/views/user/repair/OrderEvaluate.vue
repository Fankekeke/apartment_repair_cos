<template>
  <a-modal v-model="show" title="订单评价" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="repairData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>用户姓名：</b>
          {{ repairData.name }}
        </a-col>
        <a-col :span="8"><b>联系电话：</b>
          {{ repairData.phone }}
        </a-col>
        <a-col :span="8"><b>紧急程度：</b>
          <span v-if="repairData.repairLevel == 1">急</span>
          <span v-if="repairData.repairLevel == 2">重</span>
          <span v-if="repairData.repairLevel == 3">轻</span>
          <span v-if="repairData.repairLevel == 4">缓</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>维修编号：</b>
          {{ repairData.code }}
        </a-col>
        <a-col :span="16"><b>维修内容：</b>
          {{ repairData.content }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            disabled
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>维修类型：</b>
          <span v-if="repairData.repairType == 1">上下水管道</span>
          <span v-if="repairData.repairType == 2">落水管</span>
          <span v-if="repairData.repairType == 3">水箱</span>
          <span v-if="repairData.repairType == 4">天线</span>
          <span v-if="repairData.repairType == 5">供电线路</span>
          <span v-if="repairData.repairType == 6">通讯线路</span>
          <span v-if="repairData.repairType == 7">照明</span>
          <span v-if="repairData.repairType == 8">供气线路</span>
          <span v-if="repairData.repairType == 9">消防设施</span>
        </a-col>
        <a-col :span="8"><b>维修状态：</b>
          <span v-if="repairData.repairStatus == 0">未派修</span>
          <span v-if="repairData.repairStatus == 1">已派修</span>
          <span v-if="repairData.repairStatus == 2">已完成</span>
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ repairData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>房屋地址：</b>
          <a-tooltip>
            <template slot="title">
              {{ repairData.address }}
            </template>
            {{ repairData.address.slice(0, 10) }} ...
          </a-tooltip>
        </a-col>
        <a-col :span="8"><b>房间号：</b>
          {{ repairData.number }} 室
        </a-col>
        <a-col :span="8"><b>楼层：</b>
          {{ repairData.floor }} 层
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>维修时间：</b>
          {{ repairData.repairDate }}
        </a-col>
        <a-col :span="8"><b>总价格：</b>
          {{ repairData.totalPrice }} 元
        </a-col>
        <a-col :span="8"><b>支付时间：</b>
          {{ repairData.payDate }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="repairData.worker !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">维修员工信息</span></a-col>
        <a-col :span="8"><b>员工姓名：</b>
          <a-popover>
            <template slot="content">
              <a-avatar v-if="repairData.workerImage !== null" shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + repairData.workerImage.split(',')[0]" />
              <a-avatar v-else shape="square" :size="132" icon="user" />
            </template>
            <a>{{ repairData.workerName !== null ? repairData.workerName : '- -' }}</a>
          </a-popover>
        </a-col>
        <a-col :span="8"><b>联系电话：</b>
          {{ repairData.workerPhone }}
        </a-col>
      </a-row>
      <br/>
      <br/>
    </div>
    <a-form :form="form" layout="vertical" style="padding: 20px">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='评价分数' v-bind="formItemLayout">
            <a-rate v-decorator="[
            'score',
            { rules: [{ required: true, message: '请输入评价分数!' }] }
            ]" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='评价内容' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入评价内容!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='图册' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 8">
                <a-icon type="plus" />
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'evaluateAdd',
  props: {
    evaluateAddVisiable: {
      default: false
    },
    repairData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.evaluateAddVisiable
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '商品名称',
        dataIndex: 'dishesName'
      }, {
        title: '图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '购买数量',
        dataIndex: 'amount'
      }, {
        title: '单价',
        dataIndex: 'unitPrice'
      }, {
        title: '总价格',
        dataIndex: 'totalPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }]
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      durgList: [],
      logisticsList: [],
      current: 0,
      buyer: null,
      sale: null,
      commodityData: null,
      userInfo: null,
      orderInfo: null,
      merchantInfo: null,
      orderItemInfo: [],
      addressInfo: null,
      staffInfo: null,
      evaluateInfo: null
    }
  },
  watch: {
    evaluateAddVisiable: function (value) {
      if (value) {
        this.fileList = []
        if (value && this.repairData.images !== null && this.repairData.images !== '') {
          this.imagesInit(this.repairData.images)
        }
      }
    }
  },
  methods: {
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.orderId = this.repairData.id
        values.userId = this.repairData.userId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$post('/cos/evaluate-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>

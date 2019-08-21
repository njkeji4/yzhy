<template>
	<!-- batch config -->
	<el-dialog title="添加编辑黑名单" ref="modal"  width="30%" :visible.sync="modalVisible" :close-on-click-modal="false"
         class="edit-device-form">
         
		<el-form :model="batchEditForm" label-width="7em" :rules="batchEditFormRules" ref="batchEditForm">			
			<el-form-item label="姓名" prop="name" >
				<el-input type="text" v-model="batchEditForm.name" size="small" ></el-input>
			</el-form-item>
				
			<el-form-item label="身份证" prop="cardNo">
				<el-input type="text" v-model="batchEditForm.cardNo" size="small" ></el-input>
			</el-form-item>
				
			<el-form-item label="民族" prop="folk">
				<el-input type="text" v-model="batchEditForm.folk" size="small" ></el-input>
			</el-form-item>
					
			<el-form-item label="性别" prop="sex">
				<el-select clearable size="small" v-model="batchEditForm.sex" placeholder="请选择">
				    <el-option label="男" value="1"></el-option>
                    <el-option label="女" value="2"></el-option>
			    </el-select>
			</el-form-item>
			
			<el-form-item label="地址" prop="address">
				<el-input type="text" v-model="batchEditForm.address" size="small" ></el-input>
			</el-form-item>

		</el-form>

		<div slot="footer" class="dialog-footer">
			<el-button size="small" @click="modalVisible = false">取消</el-button>
			<el-button size="small" type="primary" @click="batchEdidtSubmit" :loading="batchConfigLoading">提交</el-button>
		</div>
	</el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { AdminAPI } from '../../api';

export default {
    data: function() {
        var nameCheck = (rule, value, callback) => { 
                        if(value === this.deviceInfo.name){                            
                            callback(new Error("输入不同的设备名称"));
                        }else{
                            callback();
                        }
                    };
        return {          
           modalVisible: true,			
           batchConfigLoading: false,           
           title: '',
           batchEditForm:{
               name:'',
               cardNo:'',
               folk:'',
               sex:'',              
               address:''
           },
           batchEditFormRules: {
                name: [
                    { required: true, message: '输入姓名', trigger: 'change' },                   
                ],
                cardNo: [
                    { required: true, message: '输入身份证', trigger: 'change' },                   
                ],
                sex:[
                    { required: true, message: '选择性别', trigger: 'change' },         
                ]
           },
        };
    },
	computed: {
		
	},
    created() {
        
        if(this.personInfo !== undefined){
            this.batchEditForm.name = this.personInfo.name;
            this.batchEditForm.cardNo = this.personInfo.cardNo;
            this.batchEditForm.sex = this.personInfo.sex;
            this.batchEditForm.folk = this.personInfo.folk;
            this.batchEditForm.address = this.personInfo.address;
        }            
    },
    methods: {
		batchEdidtSubmit() {            
			this.$refs.batchEditForm.validate(valid => {
				if(valid) {		                   	
					AdminAPI.updateBlacklist(
                        this.batchEditForm
                    ).then(({data}) => {
						if(data.status === 0) {
							this.$message({
								message: '操作成功!'
							});
							this.$emit('data', {});
							this.modalVisible = false;
						} else {
							this.$message.error(data.message);
						}
						
					}).catch(() => {
						this.$message.error('操作失败!');
					});
				}
			});
		},
    },
}
</script>

<style scoped lang="scss">
.edit-device-form {
    .el-dialog {
        width: 300px;
    }
    .el-dialog__body {
        padding-top: 5px;
    }
    .el-form--inline .el-form-item__content {
        width: 150px;
    }
    .el-tabs__header {
        margin-bottom: 0;
    }
}
</style>

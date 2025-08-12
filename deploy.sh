#!/bin/bash
set -e  # 出错时立即退出

# 进入项目根目录
cd "$(dirname "$0")"

echo ">>> 执行 mvn clean install"
mvn clean install

# 进入 app 模块执行 build.sh
cd s-pay-mall-ddd-app
chmod +x build.sh
echo ">>> 执行 ./build.sh"
./build.sh

# 回到 docker-compose 文件所在目录（根据你的目录结构调整）
cd ../docs/release

echo ">>> 启动环境容器"
docker-compose -f docker-compose-environment.yml up -d

echo ">>> 启动应用容器"
docker-compose -f docker-compose-app.yml up -d

echo "✅ 部署完成"

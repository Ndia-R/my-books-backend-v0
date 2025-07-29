FROM eclipse-temurin:17-jdk-jammy

RUN apt-get update && \
    apt-get install -y git curl sudo bash && \
    rm -rf /var/lib/apt/lists/*

# 既存ユーザーがいないので、新規作成
RUN useradd -m vscode

# rootユーザーで作業ディレクトリの所有者とグループを変更
USER root
WORKDIR /workspace
RUN chown vscode:vscode /workspace

# ユーザー変更
USER vscode
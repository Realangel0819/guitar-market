# 📐 최종 ERD 설계안 상세 설명 

## 1️⃣ 회원 및 인증 (User Management)

### 🔹 USER 테이블

**역할**

* 서비스의 모든 주체 (판매자, 구매자, 작성자)
* “신뢰도”와 “계정 상태”를 함께 관리

**컬럼 설명**

* `user_id (PK)`
  → 사용자 식별용 기본키
* `email (UNIQUE)`
  → 로그인 ID, 중복 불가
* `password`
  → 암호화 저장
* `nickname`
  → 게시글/댓글 노출용
* `phone`
  → 본인 인증 수단
* `phone_verified (Y/N)`
  → 인증된 판매자인지 여부
  → **중고거래 신뢰도 핵심 지표**
* `role (USER / ADMIN)`
  → 공지글, 신고 처리 등 권한 분리
* `manner_score`
  → 거래 평가 기반 평판 점수
  → 예: 36.5 (뮬 / 당근 스타일)
* `created_at`
* `deleted_at (Soft Delete)`
  → 탈퇴 후에도 **거래/분쟁 데이터 보존**

📌 **설계 의도**

> “중고거래는 신뢰가 핵심이므로, 인증·평판 정보를 User에 포함”

---

## 2️⃣ 게시글 및 상품 (Post & Product)

### 🔹 CATEGORY (자기참조)

**역할**

* 악기/장비 분류를 유연하게 관리

**컬럼**

* `category_id (PK)`
* `parent_id (FK → CATEGORY.category_id)`
* `category_name`
* `depth`

**예시**

```
일렉기타 (id=1, depth=1)
 ├─ 픽업 (id=2, parent_id=1, depth=2)
 └─ 기타부품 (id=3, parent_id=1, depth=2)
```

📌 **설계 의도**

> 자기참조 구조로 설계하여
> 카테고리 추가 시 테이블 변경 없이 확장 가능

---

### 🔹 PRODUCT (상품 정보)

**역할**

* “무엇을 파는지”에 대한 **순수 사양 정보**

**컬럼**

* `product_id (PK)`
* `category_id (FK)`
* `brand`
* `model_name`
* `description`

📌 **특징**

* 가격, 상태 ❌
* **POST와 분리**되어 재사용 가능

---

### 🔹 POST (게시글 본체)

**역할**

* 장터 글 + 커뮤니티 글을 모두 수용하는 핵심 테이블

**컬럼**

* `post_id (PK)`
* `user_id (FK → USER)`
* `product_id (FK, Nullable)`

  * 커뮤니티 글은 NULL
* `post_type`

  * MARKET / COMMUNITY / JOB
* `region`
* `location_detail`
* `sale_status`

  * SALE / RESERVED / SOLD
* `is_notice`
* `view_count`
* `created_at`
* `updated_at`
* `deleted_at`

📌 **설계 의도**

> 게시글 타입을 분리하지 않고
> `post_type`으로 통합 관리 → 확장성 ↑

---

### 🔹 POST_IMAGE

**역할**

* 게시글 이미지 다중 관리

**컬럼**

* `image_id (PK)`
* `post_id (FK)`
* `image_url`
* `sort_order`

---

## 3️⃣ 거래 프로세스 및 이력

### 🔹 TRANSACTION (실제 거래)

**역할**

* 실제 “거래 행위”를 표현하는 핵심 엔티티

**컬럼**

* `transaction_id (PK)`
* `post_id (FK)`
* `buyer_id (FK → USER)`
* `seller_id (FK → USER)`
* `trade_status`

  * REQUEST / CONFIRMED / CANCELED / COMPLETED
* `requested_at`
* `completed_at`

📌 **설계 의도**

> POST(판매글)와 TRANSACTION(거래)을 분리하여
> 상태 충돌 및 데이터 불일치 방지

---

### 🔹 TRANSACTION_STATUS_HISTORY

**역할**

* 거래 상태 변경 로그

**컬럼**

* `history_id (PK)`
* `transaction_id (FK)`
* `from_status`
* `to_status`
* `changed_at`

📌 **활용 예**

* 분쟁 발생 시 근거 데이터
* 운영/감사 로그

---

### 🔹 POST_VIEW_HISTORY

**역할**

* 조회수 중복 방지
* 사용자 관심도 분석

**컬럼**

* `view_id (PK)`
* `post_id (FK)`
* `user_id (FK, Nullable)`
* `viewed_at`

---

## 4️⃣ 사용자 소통 (Interaction)

### 🔹 COMMENT (댓글/대댓글)

**컬럼**

* `comment_id (PK)`
* `post_id (FK)`
* `user_id (FK)`
* `parent_id (FK, Nullable)`
* `content`
* `created_at`
* `deleted_at`

📌 **설계**

* parent_id로 대댓글 구현

---

### 🔹 CHAT_ROOM

**역할**

* 거래 단위 채팅방

**컬럼**

* `room_id (PK)`
* `post_id (FK)`
* `buyer_id (FK)`
* `seller_id (FK)`
* `created_at`

---

### 🔹 CHAT_MESSAGE

**컬럼**

* `message_id (PK)`
* `room_id (FK)`
* `sender_id (FK)`
* `message_content`
* `is_read`
* `sent_at`

---
